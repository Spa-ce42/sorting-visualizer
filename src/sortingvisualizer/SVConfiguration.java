package sortingvisualizer;

public class SVConfiguration {
    private int width;
    private int height;
    private String title;
    private int maxColoredBars;
    private int updateInterval;
    private boolean autoColor;

    public SVConfiguration() {
        this.width = 800;
        this.height = 450;
        this.title = "Sorting Visualizer";
        this.maxColoredBars = 2;
        this.updateInterval = 1;
        this.autoColor = true;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMaxColoredBars() {
        return maxColoredBars;
    }

    public void setMaxColoredBars(int maxColoredBars) {
        this.maxColoredBars = maxColoredBars;
    }

    public int getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(int updateInterval) {
        this.updateInterval = updateInterval;
    }

    public boolean isAutoColor() {
        return autoColor;
    }

    public void setAutoColor(boolean autoColor) {
        this.autoColor = autoColor;
    }
}
