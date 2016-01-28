(ns gilded-rose.core)

(defn classify-item [item]
  (let [tag (case (:name item)
              "Aged Brie" :brie
              "Elixir of the Mongoose" :elixir
              "Sulfuras, Hand of Ragnaros" :sulfuras
              "Backstage passes to a TAFKAL80ETC concert" :backstage
              :default)]
    (assoc item :classifier tag)))

(defmulti update-item-quality
  "Update quality for an item"
  :classifier)

(defmethod update-item-quality :sulfuras update-sulfuras-item [item] item)

(defmethod update-item-quality :brie update-brie-item [item]
  (if (< (:quality item) 50)
    (update item :quality inc)
    item))

(defmethod update-item-quality :default update-default-item [item]
  (let [quality-change (if (> 0 (:sell-in item)) 2 1)]
    (update item :quality #(- % quality-change))))

(defn update-quality
  "Calculate quality and update sell-in for a set of items"
  [items]
  (->> items
       ;; add a classifier to not depend on hardcoded strings
       (map classify-item)
       ;; decrease the sell-in counter
       (map (fn [i] (update i :sell-in dec)))
       ;; call the fn that will update the quality
       (map update-item-quality)))

(defn item [item-name, sell-in, quality]
  {:name item-name, :sell-in sell-in, :quality quality})

(defn update-current-inventory[]
  (let [inventory 
    [
      (item "+5 Dexterity Vest" 10 20)
      (item "Aged Brie" 2 0)
      (item "Elixir of the Mongoose" 5 7)
      (item "Sulfuras, Hand Of Ragnaros" 0 80)
      (item "Backstage passes to a TAFKAL80ETC concert" 15 20)
    ]]
    (update-quality inventory)
    ))
