package anteeo.com.pl.zoltansport;

/**
 * Created by user on 28.12.14.
 */
public class Order {
    String number;
    String status;

    public Order(String status, String number) {
        this.status = status;
        this.number = number;
    }
}
