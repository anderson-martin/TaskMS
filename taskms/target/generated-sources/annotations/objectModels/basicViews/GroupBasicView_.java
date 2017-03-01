package objectModels.basicViews;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import objectModels.userGroup.HierarchyGroup.STATUS;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(GroupBasicView.class)
public abstract class GroupBasicView_ {

	public static volatile SingularAttribute<GroupBasicView, String> name;
	public static volatile SingularAttribute<GroupBasicView, Long> id;
	public static volatile SingularAttribute<GroupBasicView, STATUS> status;

}

