package coupon.project.DB;

import coupon.project.beans.Coupon;
import coupon.project.beans.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepo extends JpaRepository<Customer, Integer> {

    Customer findByEmailAndPassword(String email, String password);

    Customer findByEmail(String email);

    List<Customer> findCustomerByCoupons(Coupon coupon);


}
