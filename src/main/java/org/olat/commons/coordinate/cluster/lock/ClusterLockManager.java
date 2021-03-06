/**
* OLAT - Online Learning and Training<br>
* http://www.olat.org
* <p>
* Licensed under the Apache License, Version 2.0 (the "License"); <br>
* you may not use this file except in compliance with the License.<br>
* You may obtain a copy of the License at
* <p>
* http://www.apache.org/licenses/LICENSE-2.0
* <p>
* Unless required by applicable law or agreed to in writing,<br>
* software distributed under the License is distributed on an "AS IS" BASIS, <br>
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. <br>
* See the License for the specific language governing permissions and <br>
* limitations under the License.
* <p>
* Copyright (c) since 2004 at Multimedia- & E-Learning Services (MELS),<br>
* University of Zurich, Switzerland.
* <hr>
* <a href="http://www.openolat.org">
* OpenOLAT - Online Learning and Training</a><br>
* This file has been modified by the OpenOLAT community. Changes are licensed
* under the Apache 2.0 license as the original file.
*/
package org.olat.commons.coordinate.cluster.lock;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.olat.basesecurity.IdentityRef;
import org.olat.core.commons.persistence.DB;
import org.olat.core.gui.components.Window;
import org.olat.core.id.Identity;
import org.olat.core.logging.Tracing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * Description:<br>
 * Provides the database implementation for the Locker (used only in cluster mode)
 * 
 * <P>
 * Initial Date:  10.12.2007 <br>
 * @author Felix Jost, http://www.goodsolutions.ch
 */
@Service("clusterLockManager")
public class ClusterLockManager {

	private static final Logger log = Tracing.createLoggerFor(ClusterLockManager.class);

	@Autowired
	private DB dbInstance;
	
	LockImpl findLock(String asset) {
		log.info("findLock: {} START", asset);
		String sb = "select alock from org.olat.commons.coordinate.cluster.lock.LockImpl as alock inner join fetch alock.owner where alock.asset=:asset";

		List<LockImpl> res = dbInstance.getCurrentEntityManager().createQuery(sb, LockImpl.class)
				.setParameter("asset", asset).getResultList();
		if (res.isEmpty()) {
			log.info("findLock: null END");
			return null; 
		}
		LockImpl lock = res.get(0);
		log.info("findLock: {} END", lock);
		return lock;
	}
	
	boolean isLocked(String asset) {
		String sb = "select alock.key from org.olat.commons.coordinate.cluster.lock.LockImpl as alock where alock.asset=:asset";

		List<Long> res = dbInstance.getCurrentEntityManager()
				.createQuery(sb, Long.class)
				.setParameter("asset", asset)
				.setFirstResult(0)
				.setMaxResults(1)
				.getResultList();
		return res != null && !res.isEmpty() && res.get(0) != null && res.get(0).longValue() > 0;
	}
		
	LockImpl createLockImpl(String asset, Identity owner, Window window) {
		String windowId = window == null ? null : window.getInstanceId();
		log.info("createLockImpl: {} by {} on window: {}", asset, owner, windowId);
		return new LockImpl(asset, owner, windowId);
	}
	
	void saveLock(LockImpl alock) {
		log.info("saveLock: {} START", alock);
		dbInstance.getCurrentEntityManager().persist(alock);
		log.info("saveLock: {} END", alock);
	}
	
	int deleteLock(String asset, IdentityRef owner) {
		log.info("deleteLock: {} owner: {} START", asset, owner);
		String sb = "delete from org.olat.commons.coordinate.cluster.lock.LockImpl alock where alock.owner.key=:ownerKey and alock.asset=:asset";
		int locks = dbInstance.getCurrentEntityManager()
			.createQuery(sb)
			.setParameter("ownerKey", owner.getKey())
			.setParameter("asset", asset)
			.executeUpdate();
		log.info("deleteLock: {} owner: {} END", asset, owner);
		if(locks > 0) {
			dbInstance.commit();
		}
		return locks;
	}
	
	List<LockImpl> getAllLocks() {
		log.info("getAllLocks START");
		String sb = "select alock from org.olat.commons.coordinate.cluster.lock.LockImpl as alock inner join fetch alock.owner";
		List<LockImpl> res = dbInstance.getCurrentEntityManager()
				.createQuery(sb, LockImpl.class)
				.getResultList();
		log.info("getAllLocks END. res.length: {}", (res==null ? "null" : res.size()));
		return res;
	}

	/**
	 * @param identName the name of the identity to release all locks for (only the non-persistent locks in cluster mode, -not- the persistent locks!)
	 */
	public void releaseAllLocksFor(Long identityKey) {
		log.info("releaseAllLocksFor: {} START", identityKey);	
		String sb = "delete from org.olat.commons.coordinate.cluster.lock.LockImpl alock where alock.owner.key=:ownerKey";
		int locks = dbInstance.getCurrentEntityManager().createQuery(sb)
			.setParameter("ownerKey", identityKey)
			.executeUpdate();
		// cluster:: can we save a query (and is it appropriate considering encapsulation) 
		// here by saying: alock.owner as owner where owner.name = ? (using identName parameter)
		log.info("releaseAllLocksFor: {} END ({} locks deleted)", identityKey, locks);
	}

}
