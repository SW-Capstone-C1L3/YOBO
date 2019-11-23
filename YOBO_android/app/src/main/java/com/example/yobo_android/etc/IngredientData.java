package com.example.yobo_android.etc;

public class IngredientData {
    private String ingreName;
    private int ingreImagePath;

    public IngredientData(String ingreName, int ingreImagePath) {
        this.ingreName = ingreName;
        this.ingreImagePath = ingreImagePath;
    }

    public String getIngreName() {
        return ingreName;
    }

    public void setIngreName(String ingreName) {
        this.ingreName = ingreName;
    }

    public int getIngreImagePath() {
        return ingreImagePath;
    }

    public void setIngreImagePath(int ingreImagePath) {
        this.ingreImagePath = ingreImagePath;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof IngredientData)) {
            return false;
        }
        IngredientData other = (IngredientData) o;
        return ingreName.equals(other.ingreName) && ingreImagePath == other.ingreImagePath;
    }

    public int hashCode() {
        return ingreName.hashCode();
    }
}
