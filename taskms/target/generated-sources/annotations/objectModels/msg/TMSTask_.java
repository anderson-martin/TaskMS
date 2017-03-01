package objectModels.msg;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import objectModels.msg.TMSTask.STATUS;
import objectModels.userGroup.HierarchyGroup;
import objectModels.userGroup.User;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TMSTask.class)
public abstract class TMSTask_ extends objectModels.msg.TMSMessage_ {

	public static volatile SingularAttribute<TMSTask, HierarchyGroup> senderGroup;
	public static volatile SetAttribute<TMSTask, User> recipients;
	public static volatile SingularAttribute<TMSTask, Date> dueDate;
	public static volatile SingularAttribute<TMSTask, HierarchyGroup> recipientGroup;
	public static volatile SingularAttribute<TMSTask, STATUS> status;

}

