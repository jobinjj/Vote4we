package sample.com.jobin.msi.vote4we;

public class MovieSearch {
    private String title, thumbnailUrl;


    public MovieSearch() {
    }



    public MovieSearch(String name, String thumbnailUrl) {

        this.title = name;
        this.thumbnailUrl = thumbnailUrl;


    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }


}