package ca.humber.stockbridge.model;
import jakarta.persistence.*;
@Entity public class Supplier {@Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;public String businessName;public String contactEmail;public String phone;public boolean active=true;public Supplier(){}public Supplier(String n,String e){businessName=n;contactEmail=e;}}