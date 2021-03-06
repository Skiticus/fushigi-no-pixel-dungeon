package com.fushiginopixel.fushiginopixeldungeon.windows;

import com.fushiginopixel.fushiginopixeldungeon.Warehouse;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.pots.Pot;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.scenes.PixelScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;
import com.fushiginopixel.fushiginopixeldungeon.ui.ScrollPane;
import com.watabou.noosa.Image;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

public class WndWarehouse extends WndTabbed {
    public static enum Mode {
        ALL
    }
    private static final int WIDTH		= 115;
    private static final int HEIGHT		= 120;
    private static final int ICONWIDTH		= 16;
    private static final int ICONHEIGHT		= 16;
    private WndWarehouse.WarehouseTab warehouseItems;
    private Listener listener;
    private Mode mode;
    private String title;

    public WndWarehouse(Listener listener, Mode mode, String title  ) {

        super();
        this.listener = listener;
        this.mode = mode;
        this.title = title;

        resize( WIDTH, HEIGHT );

        ArrayList<Object> items = new ArrayList<>();
        for (int i = 0; i < Warehouse.getBundle().size(); i++) {
            items.add((Object) Warehouse.getBundle().get(i));
        }

        warehouseItems = new WarehouseTab(items);
        add( warehouseItems );
        warehouseItems.setRect(0, 0, WIDTH, HEIGHT);
        warehouseItems.setupList();
        add( new LabeledTab( title ) {
            protected void select( boolean value ) {
                super.select( value );
                warehouseItems.visible = warehouseItems.active = selected;
            };
        } );

        layoutTabs();

        select( 0 );


    }

    @Override
    public void onMenuPressed() {
        if (listener == null) {
            hide();
        }
    }

    @Override
    public void onBackPressed() {
        if (listener != null) {
            listener.onSelect( null);
        }
        super.onBackPressed();
    }

    private class WarehouseTab extends Component {

        private static final int GAP = 2;

        private float pos;
        private ScrollPane itemList;
        private ArrayList<PotSlot> slots = new ArrayList<>();
        private ArrayList items;

        public WarehouseTab(ArrayList<Object> it) {
            items = it;
            itemList = new ScrollPane( new Component() ){
                @Override
                public void onClick( float x, float y ) {
                    int size = slots.size();
                    for (int i=0; i < size; i++) {
                        if (slots.get( i ).onClick( x, y )) {
                            break;
                        }
                    }
                }
            };
            add(itemList);
        }

        @Override
        protected void layout() {
            super.layout();
            itemList.setRect(0, 0, width, height);
        }

        private void setupList() {
            Component content = itemList.content();

            for (Item it : (ArrayList<Item>)items) {
                    PotSlot slot = new PotSlot(it);
                    slot.setRect(0, pos, WIDTH, ICONHEIGHT);
                    content.add(slot);
                    slots.add(slot);
                    pos += GAP + slot.height();
                }
            content.setSize(itemList.width(), pos);
            itemList.setSize(itemList.width(), itemList.height());
        }

        private class PotSlot extends Component {

            private Item item;

            Image icon;
            RenderedText txt;

            public PotSlot( Item item ){
                super();
                this.item = item;

                icon = new ItemSprite(item);
                icon.y = this.y;
                add( icon );

                txt = PixelScene.renderText( item.toString(), 8 );
                txt.x = ICONWIDTH + GAP;
                txt.y = this.y - (int)(ICONHEIGHT -  txt.baseLine()) / 2;
                add( txt );

            }

            @Override
            protected void layout() {
                super.layout();
                icon.y = this.y;
                txt.x = ICONWIDTH + GAP;
                txt.y = pos + (int)(ICONHEIGHT - txt.baseLine()) / 2;;
            }

            protected boolean onClick ( float x, float y ) {
                if (inside( x, y )) {
                    if(mode == Mode.ALL) {
                        if (item != null) {
                            GameScene.show(new WndItem(WndWarehouse.this,item));
                        }else return false;
                        return true;
                    }
                }
                return false;
            }
        }
    }

    public interface Listener {
        void onSelect(Item item);
    }
}
