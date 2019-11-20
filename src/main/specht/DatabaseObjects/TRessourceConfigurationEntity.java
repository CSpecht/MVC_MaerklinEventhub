package specht.DatabaseObjects;

import javax.persistence.*;

@Entity
@Table(name = "T_RESSOURCE_CONFIGURATION", schema = "dbo", catalog = "TRAIN_IOTHUB")
public class TRessourceConfigurationEntity {
    private int id;
    private String resourceName;
    private Integer resourceId;
    private String resourceGroup;

    @Id
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "Resource_Name")
    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    @Basic
    @Column(name = "Resource_ID")
    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    @Basic
    @Column(name = "Resource_Group")
    public String getResourceGroup() {
        return resourceGroup;
    }

    public void setResourceGroup(String resourceGroup) {
        this.resourceGroup = resourceGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TRessourceConfigurationEntity that = (TRessourceConfigurationEntity) o;

        if (id != that.id) return false;
        if (resourceName != null ? !resourceName.equals(that.resourceName) : that.resourceName != null) return false;
        if (resourceId != null ? !resourceId.equals(that.resourceId) : that.resourceId != null) return false;
        if (resourceGroup != null ? !resourceGroup.equals(that.resourceGroup) : that.resourceGroup != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (resourceName != null ? resourceName.hashCode() : 0);
        result = 31 * result + (resourceId != null ? resourceId.hashCode() : 0);
        result = 31 * result + (resourceGroup != null ? resourceGroup.hashCode() : 0);
        return result;
    }
}
