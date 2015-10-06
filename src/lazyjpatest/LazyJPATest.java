/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazyjpatest;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import lazyjpatest.entity.Customer;
import lazyjpatest.entity.DiscountCode;
import lazyjpatest.entity.PurchaseOrder;

/**
 *
 * @author kuw
 */
public class LazyJPATest {

    private final EntityManager em;
    private List<Customer> customers;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LazyJPATest test = new LazyJPATest();
        test.getCustomers();
        test.printCustomers();
        System.exit(0);
    }

    public LazyJPATest() {
        // NOTE: - Use LazyJPAOpenJPATestPU   for testing persistence unit using OpenJPA   JPA provider
        //       - Use LazyJPAHibernateTestPU for testing persistence unit using Hibernate JPA provider
        this.em = Persistence.createEntityManagerFactory("LazyJPAHibernateTestPU").createEntityManager();
    }
    public void getCustomers() {
        TypedQuery<Customer> customersQuery = em.createNamedQuery("Customer.findAll", Customer.class);
        try {
            customers = customersQuery.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printCustomers() {
        if (this.customers != null) {
            System.out.println("CUSTOMERS");
            System.out.println("==========================================");

            for (Customer customer : this.customers) {
                DiscountCode dc = customer.getDiscountCode();
                List<PurchaseOrder> purchaseOrders = customer.getPurchaseOrderList();
                System.out.print("ID: " + customer.getCustomerId());
                System.out.print(", Name: " + customer.getName());
                System.out.print(", Email: " + customer.getEmail());
                System.out.print(", Discount: " + dc.getRate() + "%");
                System.out.println("");
                if (purchaseOrders != null && purchaseOrders.size() > 0) {
                    for (PurchaseOrder po : purchaseOrders) {
                        System.out.print("   --> Order#: " + po.getOrderNum());
                        System.out.print(", Product: " + po.getProductId().getDescription());
                        System.out.print(" (" + po.getProductId().getProductCode().getDescription() + ")");
                        System.out.print(", Qty: " + po.getQuantity());
                        System.out.println("");
                    }
                } else {
                    System.out.println("    --> No purchase orders found!");
                }
            }
        }
    }    
}
