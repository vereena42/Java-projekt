import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by vereena on 1/3/16.
 */
public class SHAGenerator {

    public String getSHA(String path) throws NoSuchAlgorithmException, IOException {
        MessageDigest messageDigest=MessageDigest.getInstance("sha-256");
        byte []array= messageDigest.digest(Files.readAllBytes(new File(path).toPath()));
        StringBuilder stringBuilder=new StringBuilder();
        String temp;
        for(byte x: array){
            temp=Integer.toHexString(0xff & x);
            if(temp.length()!=2)
                stringBuilder.append("0");
            stringBuilder.append(temp);
        }
        return stringBuilder.toString();
    }
}
