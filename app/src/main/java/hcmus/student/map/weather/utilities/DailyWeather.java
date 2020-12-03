package hcmus.student.map.weather.utilities;

public class DailyWeather {
    private String icon;
    private double minTemp, maxTemp;

    public DailyWeather(int timestamp, String icon, double minTemp, double maxTemp) {
        this.icon = icon;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
    }

    public String getIcon() {
        return icon;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }
}
