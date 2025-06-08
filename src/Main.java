public class Main
{
    public static void main(String[] args) {
        MandelbrotPresenter presenter;
        if (args.length < 4) {
            presenter = new MandelbrotPresenter();
        } else {
            presenter = new MandelbrotPresenter(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        }

        MandelbrotModel model = new MandelbrotModel();
        MandelbrotView view = new MandelbrotView(presenter);

        presenter.setModelAndView(model, view);
        presenter.calc();
    }
}
