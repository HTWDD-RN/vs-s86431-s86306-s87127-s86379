public class MandelbrotPresenter
{
    MandelbrotView view;
    MandelbrotModel model;

    public void setModelAndView(MandelbrotModel model, MandelbrotView view) {
        this.model = model;
        this.view = view;
    }
}
