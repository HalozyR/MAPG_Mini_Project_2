package ece.np.edu.miniproject2;

import android.text.TextUtils;

import com.google.firebase.database.Exclude;

import org.w3c.dom.Text;

public class UploadImage {
    private String FileName;
    private String ImageUrl;
    private String mKey;

    public UploadImage(){

    }

    public UploadImage(String pFileName, String pImageUrl) {
        if(TextUtils.isEmpty(pFileName)) {
            FileName = "No Name";
        }else {
            FileName = pFileName;
        }
        ImageUrl = pImageUrl;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String pFileName) {
        FileName = pFileName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    @Exclude
    public String getKey() {
        return mKey;
    }

    @Exclude
    public void setKey(String mKey) {
        this.mKey = mKey;
    }

}
