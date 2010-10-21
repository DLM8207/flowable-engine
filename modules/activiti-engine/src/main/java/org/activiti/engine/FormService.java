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

package org.activiti.engine;

import java.util.Map;

import org.activiti.engine.form.StartForm;
import org.activiti.engine.form.TaskForm;
import org.activiti.engine.runtime.ProcessInstance;


/** Access to form data and rendered forms for starting new process instances and completing tasks.
 * 
 * @author Tom Baeyens
 */
public interface FormService {

  /** Retrieves all data necessary for rendering a form to start a new process instance. This can be used to perform rendering of the forms outside of the process engine. */
  StartForm getStartForm(String processDefinitionId);

  /** Rendered form generated by the default build-in form engine for starting a new process instance. */
  Object getRenderedStartForm(String processDefinitionId);

  /** Rendered form generated by the given build-in form engine for starting a new process instance. */
  Object getRenderedStartForm(String processDefinitionId, String formEngineName);
  
  /** Start a new process instance with the user data that was entered as properties in a start form. */  
  ProcessInstance submitStartForm(String processDefinitionId, Map<String, Object> properties);

  /** Retrieves all data necessary for rendering a form to complete a task.  This can be used to perform rendering of the forms outside of the process engine. */
  TaskForm getTaskForm(String taskId);
  
  /** Rendered form generated by the default build-in form engine for completing a task. */
  Object getRenderedTaskForm(String taskId);

  /** Rendered form generated by the given build-in form engine for completing a task. */
  Object getRenderedTaskForm(String taskId, String formEngineName);
  
  /** Completes a task with the user data that was entered as properties in a task form. */  
  void submitTaskForm(String taskId, Map<String, Object> properties);
  
}
