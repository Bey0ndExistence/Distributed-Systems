package ejb;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class CourseEntity {
    @Id
    @GeneratedValue
    private int id;
    private String denumire;
    private int nr_credite;
    public CourseEntity(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public int getNr_credite() {
        return nr_credite;
    }

    public void setNr_credite(int nr_credite) {
        this.nr_credite = nr_credite;
    }
}
