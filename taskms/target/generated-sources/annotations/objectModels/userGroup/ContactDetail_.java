package objectModels.userGroup;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ContactDetail.class)
public abstract class ContactDetail_ {

	public static volatile SingularAttribute<ContactDetail, Address> address;
	public static volatile SingularAttribute<ContactDetail, String> phoneNumber;
	public static volatile SingularAttribute<ContactDetail, String> email;

}

