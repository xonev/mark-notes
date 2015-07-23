(ns marknotes.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [clojure.string :refer [join]]))

(enable-console-print!)

(defonce app-state (atom {:text "MarkNotes"
                          :editor {:blocks ["Test one two three"]}}))

(defn editor
  [data owner]
  (reify
    om/IInitState
    (init-state [_]
      {:text ""
       :block-index 0})

    om/IRenderState
    (render-state [_ state]
      (dom/div #js {:id "editor"}
        (dom/p nil (join (:blocks data)))
        (dom/textarea #js {:onKeyPress (fn [event]
                                         (when (= (.-which event) 13)
                                           (cond
                                             (.getModifierState event "Shift")
                                             (do
                                               (om/transact! data :blocks #(assoc % (:block-index state) (:text state)))
                                               (om/update-state! owner :block-index inc)
                                               (om/set-state! owner :text "")
                                               (.preventDefault event)))))
                           :onChange #(om/set-state! owner :text (.. % -target -value))
                           :value (:text state)})))))

(defn main []
  (om/root
    (fn [app owner]
      (reify
        om/IRender
        (render [_]
          (dom/div nil
            (dom/h1 nil (:text app))
            (om/build editor (:editor app))))))
    app-state
    {:target (. js/document (getElementById "app"))}))
