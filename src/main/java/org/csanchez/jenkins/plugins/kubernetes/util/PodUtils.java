/**
 * 
 */
package org.csanchez.jenkins.plugins.kubernetes.util;

import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodCondition;
import io.fabric8.kubernetes.api.model.PodStatus;

/**
 * @author MiniKnife
 *
 */
public abstract class PodUtils {

	public static final String CONDITION_TYPE_READY = "Ready";

	public static final String[] FAILED_STATUS_OF_READY_CONDITION = { "", "" };

	public static boolean isPodFailed(Pod pod) {
		PodStatus status = pod.getStatus();

		List<PodCondition> conditions = status.getConditions();
		if (conditions == null || conditions.isEmpty()) {
			return true;
		}

		ObjectMeta meta = pod.getMetadata();
		String deletionTimestamp = meta.getDeletionTimestamp();
		if (deletionTimestamp != null) {
			return false;
		}

		for (PodCondition condition : conditions) {
			if (CONDITION_TYPE_READY.equals(condition.getType()) && condition.getStatus() != null
					&& ArrayUtils.contains(FAILED_STATUS_OF_READY_CONDITION, condition.getStatus())) {
				return true;
			}
		}

		return false;
	}
}
