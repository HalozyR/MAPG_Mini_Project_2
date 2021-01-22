package ece.np.edu.miniproject2;

public class UploadImage {
    private String FileName, ImageUrl;

    public UploadImage(){

    }

    public UploadImage(String pFileName, String pImageUrl) {
        if(pFileName == null || pFileName.isEmpty()) {
            FileName = "No Name";
        }else {
            FileName = pFileName;
        }
        ImageUrl = pImageUrl;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String FileName) {
        FileName = FileName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
