package lu.perso.menuback.data;

import lu.perso.menuback.constant.MenuEnum.UNIT;
import jakarta.persistence.*;

@Entity
public class IngredientEntity extends MenuItemEntity {
        @Column(name="sectionId")
        Long sectionId;
        @Column(name="unit")
        @Enumerated(EnumType.STRING)
        UNIT unit;
        @Column(name="isDish")
        Boolean isDish;
        // Getters et setters
        public Long getSectionId() { return sectionId; }
        public void setSectionId(Long sectionId) { this.sectionId = sectionId; }
        public UNIT getUnit() { return unit; }
        public void setUnit(UNIT unit) { this.unit = unit; }
        public Boolean getIsDish() { return isDish; }
        public void setIsDish(Boolean isDish) { this.isDish = isDish; }

        public IngredientEntity() {
                super();
        }

        public IngredientEntity(Long id, String name, Long sectionId, UNIT unit, Boolean isDish) {
                super();
                this.setId(id);
                this.setName(name);
                this.setUnit(unit);
                this.setSectionId(sectionId);
                this.setIsDish(isDish);
        }
}
