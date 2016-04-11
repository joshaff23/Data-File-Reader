package shaffer.j;

import java.math.BigDecimal;

public class Invoice 
{
    protected Integer id;
    protected String department;
    protected String description;
    protected Integer quantity;
    protected BigDecimal price;
 
    public Invoice (Integer id, String dept, String desc, Integer quant, BigDecimal price) {
    	this.id = id;
    	department = dept;
    	description = desc;
    	quantity = quant;
    	this.price = price;
    }
    
    public Integer getId() {
        return id;
    }
 
    public void setId(Integer id) {
        this.id = id;
    }
 
    public String getDepartment() {
        return department;
    }
 
    public void setDepartment(String department) {
        this.department = department;
    }
 
    public String getDescription() {
        return description;
    }
 
    public void setDescription(String description) {
        this.description = description;
    }
 
    public Integer getQuantity() {
        return quantity;
    }
 
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
 
    public BigDecimal getPrice() {
        return price;
    }
 
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public String toString() {
    	return String.format("%-7s %-12s %-34s %-3s %10.2f",
    			id, department, description,
    			quantity, price);
    }
}
