package hcmus.student.map.weather.utilities;

public class DetailWeather {
    private String icon, description;
    private double temperature, rain, wind, humidity;

    public DetailWeather(String icon, String description, double temperature, double rain, double wind, double humidity) {
        this.icon = icon;
        this.description = description;
        this.temperature = temperature;
        this.rain = rain;
        this.wind = wind;
        this.humidity = humidity;
    }

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getRain() {
        return rain;
    }

    public double getWind() {
        return wind;
    }

    public double getHumidity() {
        return humidity;
    }
}
