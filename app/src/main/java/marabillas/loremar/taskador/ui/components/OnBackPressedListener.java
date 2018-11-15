/*
 *    Copyright 2018 Loremar Marabillas
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package marabillas.loremar.taskador.ui.components;

/**
 * Listener that should be notified for back button press events.
 */
public interface OnBackPressedListener {
    /**
     * Callback invoked when the back button is pressed. The listener also gets a reference to
     * the {@link OnBackPressedInvoker} that is notifying this listener.
     *
     * @param onBackPressedInvoker the {@link OnBackPressedInvoker} that is notifying this
     *                             listener of back button press events.
     */
    void onBackPressed(OnBackPressedInvoker onBackPressedInvoker);
}
