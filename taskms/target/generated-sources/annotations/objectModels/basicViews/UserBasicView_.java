package objectModels.basicViews;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import objectModels.userGroup.User.STATUS;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(UserBasicView.class)
public abstract class UserBasicView_ {

	public static volatile SingularAttribute<UserBasicView, String> firstName;
	public static volatile SingularAttribute<UserBasicView, String> lastName;
	public static volatile SingularAttribute<UserBasicView, Long> id;
	public static volatile SingularAttribute<UserBasicView, String> userName;
	public static volatile SingularAttribute<UserBasicView, STATUS> status;

}

