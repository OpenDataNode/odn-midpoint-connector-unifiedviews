import groovy.sql.Sql;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.common.security.SecurityUtil;

// Parameters:
// The connector sends us the following:
// connection : SQL connection
// action: String correponding to the action ("CREATE" here)
// log: a handler to the Log facility
// objectClass: a String describing the Object class (__ACCOUNT__ / __GROUP__ / other)
// id: The entry identifier (OpenICF "Name" atribute. (most often matches the uid)
// attributes: an Attribute Map, containg the <String> attribute name as a key
// and the <List> attribute value(s) as value.
// password: GuardedString type
// options: a handler to the OperationOptions Map

log.info("Entering "+action+" Script");

def sql = new Sql(connection);

String newUid; //Create must return UID.

switch ( objectClass ) {
    case "__ACCOUNT__":
	
	 def maxid = 0
		   sql.eachRow("select max(id) from usr_user") { row ->
			   maxid = row[0]
		   }
		   maxid++
	
    def keys = sql.executeInsert("INSERT INTO usr_user(id,username,u_password,full_name,table_rows) VALUES (?,?,?,?,?)",
        [
            maxid,
            attributes?.get("username")?.get(0),
			SecurityUtil.decrypt(attributes?.get("__PASSWORD__")?.get(0)),
            attributes?.get("full_name")?.get(0),
            attributes?.get("table_rows")?.get(0)
         
        ])
	newUid = keys[0][0];
	
	List<String> roleNames = attributes?.get("role");
	for (String roleName : roleNames){
		sql.executeInsert("INSERT INTO usr_user_role(user_id,role_id) VALUES(?,(SELECT id FROM role WHERE name=?))",
		[
			Integer.valueOf(newUid),
			roleName
			
		]);
	}
	return newUid 
}

return newUid;
