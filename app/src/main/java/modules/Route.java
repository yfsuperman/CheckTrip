package modules;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by ling on 2016/10/30.
 */

public class Route {
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;
}
