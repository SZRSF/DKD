package com.dkd.minio;

import com.alibaba.fastjson.JSONObject;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 功能描述 文件服务器工具类
 *
 * @author:
 * @date:
 */
@Component
public class MinioUtil {

    @Autowired
    private MinioClient minioClient;

    /**
     * 查看存储bucket是否存在
     * @return boolean
     */
    public Boolean bucketExists(String bucketName) {
        Boolean found;
        try {
            found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            //e.printStackTrace();
            return false;
        }
        return found;
    }

    /**
     * 创建存储bucket
     * @return Boolean
     */
    public Boolean makeBucket(String bucketName) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * 删除存储bucket
     * @return Boolean
     */
    public Boolean removeBucket(String bucketName) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * 获取全部bucket
     */
    public List<Bucket> getAllBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 文件上传
     * @param file 文件
     * @return Boolean
     */
    public Boolean upload(String bucketName, String fileName, MultipartFile file) {
        try {
            PutObjectArgs objectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(file.getInputStream(),file.getSize(),-1)
                    .contentType(file.getContentType())
                    .build();
            //文件名称相同会覆盖
            minioClient.putObject(objectArgs);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 预览图片
     * @param fileName
     * @return
     */
    public String preview(String fileName,String bucketName){
        // 查看文件地址
        GetPresignedObjectUrlArgs build = new GetPresignedObjectUrlArgs().builder().bucket(bucketName).object(fileName).method(Method.GET).build();
        try {
            String url = minioClient.getPresignedObjectUrl(build);
            return url;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 文件下载
     * @param fileName 文件名称
     * @param res response
     * @return Boolean
     */
    public void download(String fileName,String bucketName, HttpServletResponse res) {
        GetObjectArgs objectArgs = GetObjectArgs.builder().bucket(bucketName)
                .object(fileName).build();
        try (GetObjectResponse response = minioClient.getObject(objectArgs)){
            byte[] buf = new byte[1024];
            int len;
            try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()){
                while ((len=response.read(buf))!=-1){
                    os.write(buf,0,len);
                }
                os.flush();
                byte[] bytes = os.toByteArray();
                res.setCharacterEncoding("utf-8");
                //设置强制下载不打开
                //res.setContentType("application/force-download");
                res.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
                try (ServletOutputStream stream = res.getOutputStream()){
                    stream.write(bytes);
                    stream.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查看文件对象
     * @return 存储bucket内文件对象信息
     */
    public List<Item> listObjects(String bucketName) {
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(bucketName).build());
        List<Item> items = new ArrayList<>();
        try {
            for (Result<Item> result : results) {
                items.add(result.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return items;
    }

    /**
     * 删除
     * @param fileName
     * @return
     * @throws Exception
     */
    public boolean remove(String fileName,String bucketName){
        try {
            minioClient.removeObject( RemoveObjectArgs.builder().bucket(bucketName).object(fileName).build());
        }catch (Exception e){
            return false;
        }
        return true;
    }

    /**
     * 批量删除文件对象（没测试）
     * @param objects 对象名称集合
     */
    public Iterable<Result<DeleteError>> removeObjects(List<String> objects, String bucketName) {
        List<DeleteObject> dos = objects.stream().map(e -> new DeleteObject(e)).collect(Collectors.toList());
        Iterable<Result<DeleteError>> results = minioClient.removeObjects(RemoveObjectsArgs.builder().bucket(bucketName).objects(dos).build());
        return results;
    }

    //上传文件，返回路径
    public Object upload(HashMap<String, Object> paramMap) {
        MultipartFile file=(MultipartFile)paramMap.get("file");
        if (file.isEmpty() || file.getSize() == 0) {
            return "文件为空";
        }
        try {
            if (!this.bucketExists("file")) {
                this.makeBucket("file");
            }
            InputStream inputStream = file.getInputStream();
            String fileName = file.getOriginalFilename();

            String newName = UUID.randomUUID().toString().replaceAll("-", "")
                    + fileName.substring(fileName.lastIndexOf("."));

            this.upload("file", newName,file);
            inputStream.close();
            String fileUrl = this.preview(newName,"file");
            String fileUrlNew=fileUrl.substring(0, fileUrl.indexOf("?"));
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("url",fileUrlNew);
            return jsonObject.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
            return "上传失败";
        }
    }
}