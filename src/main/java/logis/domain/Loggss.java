package logis.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Loggss.
 */
@Entity
@Table(name = "loggss")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Loggss implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_venta")
    private Integer idVenta;

    @Column(name = "paso")
    private String paso;

    @Column(name = "resultado")
    private String resultado;

    @Column(name = "explicacion")
    private String explicacion;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdVenta() {
        return idVenta;
    }

    public Loggss idVenta(Integer idVenta) {
        this.idVenta = idVenta;
        return this;
    }

    public void setIdVenta(Integer idVenta) {
        this.idVenta = idVenta;
    }

    public String getPaso() {
        return paso;
    }

    public Loggss paso(String paso) {
        this.paso = paso;
        return this;
    }

    public void setPaso(String paso) {
        this.paso = paso;
    }

    public String getResultado() {
        return resultado;
    }

    public Loggss resultado(String resultado) {
        this.resultado = resultado;
        return this;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getExplicacion() {
        return explicacion;
    }

    public Loggss explicacion(String explicacion) {
        this.explicacion = explicacion;
        return this;
    }

    public void setExplicacion(String explicacion) {
        this.explicacion = explicacion;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Loggss)) {
            return false;
        }
        return id != null && id.equals(((Loggss) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Loggss{" +
            "id=" + getId() +
            ", idVenta=" + getIdVenta() +
            ", paso='" + getPaso() + "'" +
            ", resultado='" + getResultado() + "'" +
            ", explicacion='" + getExplicacion() + "'" +
            "}";
    }
}
