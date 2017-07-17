/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flowable.dmn.engine.impl.cmd;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.flowable.dmn.api.DmnDecisionTable;
import org.flowable.dmn.engine.impl.ExecuteDecisionBuilderImpl;
import org.flowable.dmn.engine.impl.ExecuteDecisionInfo;
import org.flowable.dmn.engine.impl.persistence.deploy.DecisionTableCacheEntry;
import org.flowable.dmn.engine.impl.persistence.deploy.DeploymentManager;
import org.flowable.dmn.model.Decision;
import org.flowable.engine.common.api.FlowableObjectNotFoundException;

/**
 * @author Yvo Swillens
 */
public abstract class AbstractExecuteDecisionCmd implements Serializable {

    private static final long serialVersionUID = 1L;

    protected ExecuteDecisionInfo executeDecisionInfo = new ExecuteDecisionInfo();
    
    public AbstractExecuteDecisionCmd(ExecuteDecisionBuilderImpl decisionBuilder) {
        executeDecisionInfo.setDecisionKey(decisionBuilder.getDecisionKey());
        executeDecisionInfo.setParentDeploymentId(decisionBuilder.getParentDeploymentId());
        executeDecisionInfo.setInstanceId(decisionBuilder.getInstanceId());
        executeDecisionInfo.setExecutionId(decisionBuilder.getExecutionId());
        executeDecisionInfo.setActivityId(decisionBuilder.getActivityId());
        executeDecisionInfo.setVariables(decisionBuilder.getVariables());
        executeDecisionInfo.setTenantId(decisionBuilder.getTenantId());
    }
    
    public AbstractExecuteDecisionCmd(String decisionKey, Map<String, Object> variables) {
        executeDecisionInfo.setDecisionKey(decisionKey);
        executeDecisionInfo.setVariables(variables);
    }

    protected DmnDecisionTable resolveDecisionTable(DeploymentManager deploymentManager) {
        DmnDecisionTable decisionTable = null;

        if (StringUtils.isNotEmpty(getDecisionKey()) && StringUtils.isNotEmpty(getParentDeploymentId()) && StringUtils.isNotEmpty(getTenantId())) {
            decisionTable = deploymentManager.findDeployedLatestDecisionByKeyParentDeploymentIdAndTenantId(
                            getDecisionKey(), getParentDeploymentId(), getTenantId());
            if (decisionTable == null) {
                throw new FlowableObjectNotFoundException("No decision found for key: " + getDecisionKey() +
                    ", parent deployment id " + getParentDeploymentId() + " and tenant id: " + getTenantId());
            }

        } else if (StringUtils.isNotEmpty(getDecisionKey()) && StringUtils.isNotEmpty(getParentDeploymentId())) {
            decisionTable = deploymentManager.findDeployedLatestDecisionByKeyAndParentDeploymentId(getDecisionKey(), getParentDeploymentId());
            if (decisionTable == null) {
                throw new FlowableObjectNotFoundException("No decision found for key: " + getDecisionKey() +
                    " and parent deployment id " + getParentDeploymentId());
            }

        } else if (StringUtils.isNotEmpty(getDecisionKey()) && StringUtils.isNotEmpty(getTenantId())) {
            decisionTable = deploymentManager.findDeployedLatestDecisionByKeyAndTenantId(getDecisionKey(), getTenantId());
            if (decisionTable == null) {
                throw new FlowableObjectNotFoundException("No decision found for key: " + getDecisionKey() +
                    " and tenant id " + getTenantId());
            }

        } else if (StringUtils.isNotEmpty(getDecisionKey())) {
            decisionTable = deploymentManager.findDeployedLatestDecisionByKey(getDecisionKey());
            if (decisionTable == null) {
                throw new FlowableObjectNotFoundException("No decision found for key: " + getDecisionKey());
            }

        } else {
            throw new IllegalArgumentException("decisionKey is null");
        }
        
        executeDecisionInfo.setDecisionDefinitionId(decisionTable.getId());
        executeDecisionInfo.setDeploymentId(decisionTable.getDeploymentId());

        return decisionTable;
    }

    protected Decision resolveDecision(DeploymentManager deploymentManager, DmnDecisionTable decisionTable) {
        if (decisionTable == null) {
            throw new IllegalArgumentException("decisionTable is null");
        }

        DecisionTableCacheEntry decisionTableCacheEntry = deploymentManager.resolveDecisionTable(decisionTable);
        Decision decision = decisionTableCacheEntry.getDecision();

        return decision;
    }
    
    protected String getDecisionKey() {
        return executeDecisionInfo.getDecisionKey();
    }
    
    protected String getParentDeploymentId() {
        return executeDecisionInfo.getParentDeploymentId();
    }
    
    protected String getTenantId() {
        return executeDecisionInfo.getTenantId();
    }
}