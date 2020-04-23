/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rg.quintana.proyectofxml;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author josem
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "JUGADORES")
@javax.persistence.NamedQueries({
    @javax.persistence.NamedQuery(name = "Jugadores.findAll", query = "SELECT j FROM Jugadores j"),
    @javax.persistence.NamedQuery(name = "Jugadores.findById", query = "SELECT j FROM Jugadores j WHERE j.id = :id"),
    @javax.persistence.NamedQuery(name = "Jugadores.findByNombre", query = "SELECT j FROM Jugadores j WHERE j.nombre = :nombre"),
    @javax.persistence.NamedQuery(name = "Jugadores.findByApellidos", query = "SELECT j FROM Jugadores j WHERE j.apellidos = :apellidos"),
    @javax.persistence.NamedQuery(name = "Jugadores.findByNacionalidad", query = "SELECT j FROM Jugadores j WHERE j.nacionalidad = :nacionalidad"),
    @javax.persistence.NamedQuery(name = "Jugadores.findByValor", query = "SELECT j FROM Jugadores j WHERE j.valor = :valor"),
    @javax.persistence.NamedQuery(name = "Jugadores.findByFoto", query = "SELECT j FROM Jugadores j WHERE j.foto = :foto"),
    @javax.persistence.NamedQuery(name = "Jugadores.findByDisponible", query = "SELECT j FROM Jugadores j WHERE j.disponible = :disponible")})
public class Jugadores implements Serializable {

    private static final long serialVersionUID = 1L;
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @javax.persistence.Basic(optional = false)
    @javax.persistence.Column(name = "ID")
    private Integer id;
    @javax.persistence.Basic(optional = false)
    @javax.persistence.Column(name = "NOMBRE")
    private String nombre;
    @javax.persistence.Basic(optional = false)
    @javax.persistence.Column(name = "APELLIDOS")
    private String apellidos;
    @javax.persistence.Column(name = "NACIONALIDAD")
    private String nacionalidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @javax.persistence.Column(name = "VALOR")
    private BigDecimal valor;
    @javax.persistence.Column(name = "FOTO")
    private String foto;
    @javax.persistence.Column(name = "DISPONIBLE")
    private Boolean disponible;
    @javax.persistence.JoinColumn(name = "CLUB", referencedColumnName = "ID")
    @javax.persistence.ManyToOne(optional = false)
    private Club club;

    public Jugadores() {
    }

    public Jugadores(Integer id) {
        this.id = id;
    }

    public Jugadores(Integer id, String nombre, String apellidos) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
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

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
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
        if (!(object instanceof Jugadores)) {
            return false;
        }
        Jugadores other = (Jugadores) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "rg.quintana.proyectofxml.Jugadores[ id=" + id + " ]";
    }
    
}
