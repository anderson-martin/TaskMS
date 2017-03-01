package objectModels.userGroup;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import objectModels.userGroup.HierarchyGroup.STATUS;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(HierarchyGroup.class)
public abstract class HierarchyGroup_ {

	public static volatile SingularAttribute<HierarchyGroup, String> name;
	public static volatile SetAttribute<HierarchyGroup, HierarchyGroup> subordinateGroups;
	public static volatile SingularAttribute<HierarchyGroup, Long> id;
	public static volatile SingularAttribute<HierarchyGroup, HierarchyGroup> managerGroup;
	public static volatile SingularAttribute<HierarchyGroup, STATUS> status;

}

