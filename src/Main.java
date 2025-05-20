public class Main
{
    public static void main(String[] args) {
        MandelbrotPresenter presenter = new MandelbrotPresenter();
        MandelbrotModel model = new MandelbrotModel();
        MandelbrotView view = new MandelbrotView(presenter);

        presenter.setModelAndView(model, view);
    }
}
