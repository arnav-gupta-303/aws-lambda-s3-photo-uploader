import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;

import java.util.Base64;

public class UploadPhotoHandler implements RequestHandler<APIGatewayV2HTTPEvent, String> {

    private final S3Client s3 = S3Client.builder()
            .region(Region.AP_SOUTH_1)
            .build();

    public String handleRequest(APIGatewayV2HTTPEvent event, Context context) {

        try {

            String fileName = "uploaded-image.jpg";

            String body = event.getBody();

            byte[] imageBytes;

            // API Gateway sends binary as base64
            if (event.getIsBase64Encoded() && event.getIsBase64Encoded()) {
                imageBytes = Base64.getDecoder().decode(body);
            } else {
                imageBytes = body.getBytes();
            }

            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket("my-file-storage-uploads")
                    .key(fileName)
                    .contentType("image/jpeg")
                    .build();

            s3.putObject(putRequest, RequestBody.fromBytes(imageBytes));

            return "Image uploaded successfully";

        } catch (Exception e) {
            e.printStackTrace();
            return "Upload failed";
        }
    }
}