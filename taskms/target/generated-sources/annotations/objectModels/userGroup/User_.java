package objectModels.userGroup;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import objectModels.userGroup.User.STATUS;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public abstract class User_ {

	public static volatile SingularAttribute<User, String> firstName;
	public static volatile SingularAttribute<User, String> lastName;
	public static volatile SingularAttribute<User, ContactDetail> contactDetail;
	public static volatile SetAttribute<User, HierarchyGroup> groups;
	public static volatile SingularAttribute<User, Long> id;
	public static volatile SingularAttribute<User, String> userName;
	public static volatile SingularAttribute<User, STATUS> status;

}

