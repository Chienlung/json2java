package com.example.protocol;
public class AdResponse {
  private int resultCode;
  public int getResultCode() {
    return resultCode;
  }
  public void setResultCode(int resultCode) {
    this.resultCode = resultCode;
  }
  private String adType;
  public String getAdType() {
    return adType;
  }
  public void setAdType(String adType) {
    this.adType = adType;
  }
  private String adMaterial;
  public String getAdMaterial() {
    return adMaterial;
  }
  public void setAdMaterial(String adMaterial) {
    this.adMaterial = adMaterial;
  }
  private Image image;
  public Image getImage() {
    return image;
  }
  public void setImage(Image image) {
    this.image = image;
  }
  private List < String > impressionUrls;
  public List < String > getImpressionUrls() {
    return impressionUrls;
  }
  public void setImpressionUrls(List < String > impressionUrls) {
    this.impressionUrls = impressionUrls;
  }
  private List < String > clickUrls;
  public List < String > getClickUrls() {
    return clickUrls;
  }
  public void setClickUrls(List < String > clickUrls) {
    this.clickUrls = clickUrls;
  }
  public static class Image {
    private int width;
    public int getWidth() {
      return width;
    }
    public void setWidth(int width) {
      this.width = width;
    }
    private int height;
    public int getHeight() {
      return height;
    }
    public void setHeight(int height) {
      this.height = height;
    }
    private String imgSrc;
    public String getImgSrc() {
      return imgSrc;
    }
    public void setImgSrc(String imgSrc) {
      this.imgSrc = imgSrc;
    }
    private String landingUrl;
    public String getLandingUrl() {
      return landingUrl;
    }
    public void setLandingUrl(String landingUrl) {
      this.landingUrl = landingUrl;
    }
  }
}