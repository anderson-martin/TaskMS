package objectModels.msg;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import objectModels.msg.TMSIssue.STATUS;
import objectModels.userGroup.HierarchyGroup;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TMSIssue.class)
public abstract class TMSIssue_ extends objectModels.msg.TMSMessage_ {

	public static volatile SingularAttribute<TMSIssue, HierarchyGroup> senderGroup;
	public static volatile SingularAttribute<TMSIssue, HierarchyGroup> recipientGroup;
	public static volatile SingularAttribute<TMSIssue, STATUS> status;

}

