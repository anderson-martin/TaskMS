package objectModels.msg;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import objectModels.userGroup.User;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TMSMessage.class)
public abstract class TMSMessage_ {

	public static volatile SingularAttribute<TMSMessage, Date> sentDate;
	public static volatile SingularAttribute<TMSMessage, User> sender;
	public static volatile SingularAttribute<TMSMessage, Long> id;
	public static volatile SingularAttribute<TMSMessage, String> title;
	public static volatile SingularAttribute<TMSMessage, String> content;

}

