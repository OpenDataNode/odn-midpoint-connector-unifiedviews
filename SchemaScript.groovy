/*
 * Copyright (c) 2010-2013 Evolveum
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.identityconnectors.common.logging.Log
import org.identityconnectors.framework.common.objects.AttributeInfo
import org.identityconnectors.framework.common.objects.AttributeInfoBuilder
import org.identityconnectors.framework.common.objects.ObjectClass
import org.identityconnectors.framework.common.objects.ObjectClassInfoBuilder
import org.identityconnectors.framework.common.objects.OperationalAttributeInfos

def log = log as Log

log.ok("Schema script execution started.");

Set<AttributeInfo.Flags> flags = new HashSet<AttributeInfo.Flags>();
flags.add(AttributeInfo.Flags.MULTIVALUED);

ObjectClassInfoBuilder account = new ObjectClassInfoBuilder();
account.setType(ObjectClass.ACCOUNT_NAME);
account.addAttributeInfo(AttributeInfoBuilder.build("username", String.class));
account.addAttributeInfo(AttributeInfoBuilder.build("full_name", String.class));
account.addAttributeInfo(AttributeInfoBuilder.build("table_rows", Integer.class));
account.addAttributeInfo(AttributeInfoBuilder.build("role", String.class, flags));
account.addAttributeInfo(OperationalAttributeInfos.PASSWORD);
builder.defineObjectClass(account.build());

log.ok("Schema script execution finished.");
