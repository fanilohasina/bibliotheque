package com.java.bibliotheque.entite;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "livres")
public class Livre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;

    private LocalDate dateDePublication;

    private LocalDate dateDentree;

    private int categorieAge;

    @ManyToOne
    @JoinColumn(name = "id_auteur")
    private Auteur auteur;

    @ManyToMany
    @JoinTable(name = "livres_categories", joinColumns = @JoinColumn(name = "livre_id"), inverseJoinColumns = @JoinColumn(name = "categorie_id"))
    private Set<Categorie> categories = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "id_langue")
    private Langue langue;

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public LocalDate getDateDePublication() {
        return dateDePublication;
    }

    public void setDateDePublication(LocalDate dateDePublication) {
        this.dateDePublication = dateDePublication;
    }

    public LocalDate getDateDentree() {
        return dateDentree;
    }

    public void setDateDentree(LocalDate dateDentree) {
        this.dateDentree = dateDentree;
    }

    public Auteur getAuteur() {
        return auteur;
    }

    public void setAuteur(Auteur auteur) {
        this.auteur = auteur;
    }

    public Set<Categorie> getCategories() {
        return categories;
    }

    public void setCategories(Set<Categorie> categories) {
        this.categories = categories;
    }

    public Langue getLangue() {
        return langue;
    }

    public void setLangue(Langue langue) {
        this.langue = langue;
    }

    public int getCategorieAge() {
        return categorieAge;
    }

    public void setCategorieAge(int categorieAge) {
        this.categorieAge = categorieAge;
    }
}
