/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rg.quintana.proyectofxml;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author josem
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "CLUB")
@javax.persistence.NamedQueries({
    @javax.persistence.NamedQuery(name = "Club.findAll", query = "SELECT c FROM Club c"),
    @javax.persistence.NamedQuery(name = "Club.findById", query = "SELECT c FROM Club c WHERE c.id = :id"),
    @javax.persistence.NamedQuery(name = "Club.findByNombre", query = "SELECT c FROM Club c WHERE c.nombre = :nombre"),
    @javax.persistence.NamedQuery(name = "Club.findByFecfundacion", query = "SELECT c FROM Club c WHERE c.fecfundacion = :fecfundacion")})
public class Club implements Serializable {

    private static final long serialVersionUID = 1L;
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @javax.persistence.Basic(optional = false)
    @javax.persistence.Column(name = "ID")
    private Integer id;
    @javax.persistence.Basic(optional = false)
    @javax.persistence.Column(name = "NOMBRE")
    private String nombre;
    @javax.persistence.Column(name = "FECFUNDACION")
    @javax.persistence.Temporal(javax.persistence.TemporalType.DATE)
    private Date fecfundacion;
    @javax.persistence.OneToMany(cascade = javax.persistence.CascadeType.ALL, mappedBy = "club")
    private Collection<Jugadores> jugadoresCollection;

    public Club() {
    }

    public Club(Integer id) {
        this.id = id;
    }

    public Club(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFecfundacion() {
        return fecfundacion;
    }

    public void setFecfundacion(Date fecfundacion) {
        this.fecfundacion = fecfundacion;
    }

    public Collection<Jugadores> getJugadoresCollection() {
        return jugadoresCollection;
    }

    public void setJugadoresCollection(Collection<Jugadores> jugadoresCollection) {
        this.jugadoresCollection = jugadoresCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Club)) {
            return false;
        }
        Club other = (Club) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "rg.quintana.proyectofxml.Club[ id=" + id + " ]";
    }
    
}
