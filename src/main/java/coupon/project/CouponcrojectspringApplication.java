package coupon.project;

import coupon.project.DB.CompanyDBDAO;
import coupon.project.DB.CouponDBDAO;
import coupon.project.beans.Category;
import coupon.project.beans.Company;
import coupon.project.beans.Coupon;
import coupon.project.beans.Customer;
import coupon.project.facades.AdminFacade;
import coupon.project.facades.CompanyFacade;
import coupon.project.facades.CustomerFacade;
import coupon.project.facades.ExpiredCouponDelete;
import coupon.project.login.ClientType;
import coupon.project.login.LoginManger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Calendar;
import java.util.Date;

@SpringBootApplication
public class CouponcrojectspringApplication {

    public static Date getToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static Date getTomorrow() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();

    }

    public static void main(String[] args) {

        ConfigurableApplicationContext ctx = SpringApplication.run(CouponcrojectspringApplication.class, args);
        ExpiredCouponDelete expiredCouponDelete = new ExpiredCouponDelete();
        Thread t = new Thread(expiredCouponDelete);
        t.start();
        try {

            LoginManger loginManger = ctx.getBean(LoginManger.class);

            //Admin
            AdminFacade admin = (AdminFacade) loginManger.login("admin@admin.com", "admin", ClientType.Admin);

            //Add customer - works
            Customer c1 = new Customer("Daniel", "Shatz", "DanielShatz@gmail.com", "123");
            admin.addCustomer(c1);
            //System.out.println("Added customer with info:");
            //System.out.println(c1.toString());
            //Update customer - works
            //System.out.println("Customer email before update: " + c1.getEmail());
            //c1.setEmail("DanielUpdated@gmail.com");
            //System.out.println("Customer email after update: " + c1.getEmail());
            //admin.updateCustomer(c1);
            //Print all customers - works
            //System.out.println(admin.getAllCustomers());
            //Delete customer - works
            //admin.deleteCustomer(c1.getId());
            //Check if customer exists by email and password - works
            //System.out.println(admin.isCustomerExists("DanielShatz@gmail.com", "123"));

            //Add company - works
            Company company1 = new Company("Shatz ltd", "shatz@shatz.com", "123");
            admin.addCompany(company1);
            //Update company - works
            //System.out.println("Company name before update: " + company1.getName());
            //company1.setName("Shatz Updated ltd");
            //System.out.println("Company name after update" + company1.getName());
            //admin.updateCompany();
            //Print all companies - works
            //System.out.println(admin.GetAllCompanies());
            //Delete company - works
            //admin.deleteCompany(company1.getId());

            //Company
            CompanyDBDAO co1 = ctx.getBean(CompanyDBDAO.class);
            CouponDBDAO cou1 = ctx.getBean(CouponDBDAO.class);
            CompanyFacade companyFacade = (CompanyFacade) loginManger.login("shatz@shatz.com", "123",
                    ClientType.Company);
            //Add coupon - works
            //Static dates to make the coupon valid for 24h from the creation date for testing
            Date today = getToday();
            Date tomorrow = getTomorrow();
            Coupon coupon1 = new Coupon(company1, 100, "test coupon", "test description",
                    "image.jpg", Category.Electricity, today, tomorrow, 555.5);
            companyFacade.addCoupon(coupon1);
            System.out.println("coupon1 ID:" + coupon1.getId());
            //Update coupon - works
            System.out.println("Coupon amount before update: " + coupon1.getAmount());
            coupon1.setAmount(6000);
            companyFacade.updateCoupon(coupon1);
            System.out.println("Coupon amount after update: " + coupon1.getAmount());
            //Print all coupons for company TODO add this method
            //System.out.println(cou1.getCouponByCompany(company1));
            //Delete coupon - works
            //companyFacade.deleteCoupon(coupon1.getId());

            //Customer
            CustomerFacade customerFacade = (CustomerFacade) loginManger.login("DanielShatz@gmail.com", "123",
                    ClientType.Customer);
            //Purchase coupon - works
            customerFacade.purchaseCoupon(coupon1);
            //Delete coupon purchase
            //customerFacade.deleteCouponPurchase(coupon1);

        } catch (Exception e) {
            System.out.println(e.getMessage()); //printStackTrace();
        } finally {
            expiredCouponDelete.stop();
        }


    }

}
