package at.ac.tuwien.ims.sinking.GameEngine.UI;

import java.util.ArrayList;

/**
 * Button class</br>
 * @author Matthias Huerbe
 */
public class UIButton extends UIElement {
    private ArrayList<ButtonEvent> onClickEvents;
    private ArrayList<ButtonEvent> onReleaseEvents;

    public UIButton()
    {
        super();

        onClickEvents = new ArrayList<>();
        onReleaseEvents = new ArrayList<>();
    }

    public void addOnClickEvent(ButtonEvent buttonEvent)
    {
        onClickEvents.add(buttonEvent);
    }

    public void addOnReleasedEvent(ButtonEvent buttonEvent)
    {
        onReleaseEvents.add(buttonEvent);
    }

    @Override
    public void onClicked() {
        super.onClicked();

        if(onClickEvents == null)
            return;

        for(int i = 0; i < onClickEvents.size(); i++)
        {
           onClickEvents.get(i).execute();
        }
    }

    @Override
    public void onReleased() {
        super.onReleased();

        if(onReleaseEvents == null)
            return;

        for(int i = 0; i < onReleaseEvents.size(); i++)
        {
            onReleaseEvents.get(i).execute();
        }
    }
}
