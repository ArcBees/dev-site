package com.google.gwt.site.demo.gsss.mixins.examples;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.site.demo.gsss.mixins.resources.MixinsResources;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class Examples implements IsWidget {
    @UiTemplate("Example1.ui.xml")
    interface Example1Binder extends UiBinder<Widget, Examples> {
    }

    @UiTemplate("Example2.ui.xml")
    interface Example2Binder extends UiBinder<Widget, Examples> {
    }

    @UiTemplate("Example3.ui.xml")
    interface Example3Binder extends UiBinder<Widget, Examples> {
    }

    @UiTemplate("Example4.ui.xml")
    interface Example4Binder extends UiBinder<Widget, Examples> {
    }

    @UiTemplate("Example5.ui.xml")
    interface Example5Binder extends UiBinder<Widget, Examples> {
    }

    @UiTemplate("Example6.ui.xml")
    interface Example6Binder extends UiBinder<Widget, Examples> {
    }

    @UiTemplate("Example7.ui.xml")
    interface Example7Binder extends UiBinder<Widget, Examples> {
    }

    @UiTemplate("Example8.ui.xml")
    interface Example8Binder extends UiBinder<Widget, Examples> {
    }

    @UiTemplate("Example9.ui.xml")
    interface Example9Binder extends UiBinder<Widget, Examples> {
    }

    @UiTemplate("Example10.ui.xml")
    interface Example10Binder extends UiBinder<Widget, Examples> {
    }

    @UiTemplate("Example11.ui.xml")
    interface Example11Binder extends UiBinder<Widget, Examples> {
    }

    @UiTemplate("Example12.ui.xml")
    interface Example12Binder extends UiBinder<Widget, Examples> {
    }

    @UiTemplate("Example13.ui.xml")
    interface Example13Binder extends UiBinder<Widget, Examples> {
    }

    @UiTemplate("Example14.ui.xml")
    interface Example14Binder extends UiBinder<Widget, Examples> {
    }

    @UiTemplate("Example15.ui.xml")
    interface Example15Binder extends UiBinder<Widget, Examples> {
    }

    @UiTemplate("Example16.ui.xml")
    interface Example16Binder extends UiBinder<Widget, Examples> {
    }

    @UiTemplate("Example17.ui.xml")
    interface Example17Binder extends UiBinder<Widget, Examples> {
    }

    @UiTemplate("Example18.ui.xml")
    interface Example18Binder extends UiBinder<Widget, Examples> {
    }

    @UiTemplate("Example19.ui.xml")
    interface Example19Binder extends UiBinder<Widget, Examples> {
    }

    @UiTemplate("Example20.ui.xml")
    interface Example20Binder extends UiBinder<Widget, Examples> {
    }

    @UiTemplate("Example21.ui.xml")
    interface Example21Binder extends UiBinder<Widget, Examples> {
    }

    @UiTemplate("Example22.ui.xml")
    interface Example22Binder extends UiBinder<Widget, Examples> {
    }

    @UiTemplate("Example23.ui.xml")
    interface Example23Binder extends UiBinder<Widget, Examples> {
    }

    @UiTemplate("Example24.ui.xml")
    interface Example24Binder extends UiBinder<Widget, Examples> {
    }

    @UiTemplate("Example25.ui.xml")
    interface Example25Binder extends UiBinder<Widget, Examples> {
    }

    @UiTemplate("Example26.ui.xml")
    interface Example26Binder extends UiBinder<Widget, Examples> {
    }

    @UiTemplate("Example27.ui.xml")
    interface Example27Binder extends UiBinder<Widget, Examples> {
    }

    @UiTemplate("Example28.ui.xml")
    interface Example28Binder extends UiBinder<Widget, Examples> {
    }

    @UiTemplate("Example29.ui.xml")
    interface Example29Binder extends UiBinder<Widget, Examples> {
    }

    private static Example1Binder example1Binder = GWT.create(Example1Binder.class);
    private static Example2Binder example2Binder = GWT.create(Example2Binder.class);
    private static Example3Binder example3Binder = GWT.create(Example3Binder.class);
    private static Example4Binder example4Binder = GWT.create(Example4Binder.class);
    private static Example5Binder example5Binder = GWT.create(Example5Binder.class);
    private static Example6Binder example6Binder = GWT.create(Example6Binder.class);
    private static Example7Binder example7Binder = GWT.create(Example7Binder.class);
    private static Example8Binder example8Binder = GWT.create(Example8Binder.class);
    private static Example9Binder example9Binder = GWT.create(Example9Binder.class);
    private static Example10Binder example10Binder = GWT.create(Example10Binder.class);
    private static Example11Binder example11Binder = GWT.create(Example11Binder.class);
    private static Example12Binder example12Binder = GWT.create(Example12Binder.class);
    private static Example13Binder example13Binder = GWT.create(Example13Binder.class);
    private static Example14Binder example14Binder = GWT.create(Example14Binder.class);
    private static Example15Binder example15Binder = GWT.create(Example15Binder.class);
    private static Example16Binder example16Binder = GWT.create(Example16Binder.class);
    private static Example17Binder example17Binder = GWT.create(Example17Binder.class);
    private static Example18Binder example18Binder = GWT.create(Example18Binder.class);
    private static Example19Binder example19Binder = GWT.create(Example19Binder.class);
    private static Example20Binder example20Binder = GWT.create(Example20Binder.class);
    private static Example21Binder example21Binder = GWT.create(Example21Binder.class);
    private static Example22Binder example22Binder = GWT.create(Example22Binder.class);
    private static Example23Binder example23Binder = GWT.create(Example23Binder.class);
    private static Example24Binder example24Binder = GWT.create(Example24Binder.class);
    private static Example25Binder example25Binder = GWT.create(Example25Binder.class);
    private static Example26Binder example26Binder = GWT.create(Example26Binder.class);
    private static Example27Binder example27Binder = GWT.create(Example27Binder.class);
    private static Example28Binder example28Binder = GWT.create(Example28Binder.class);
    private static Example29Binder example29Binder = GWT.create(Example29Binder.class);

    private final Widget widget;

    @UiField
    MixinsResources resources;

    protected Examples(UiBinder<Widget, Examples> binder) {
        widget = binder.createAndBindUi(this);

        resources.style().ensureInjected();
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    public static IsWidget createExample1() {
        return new Examples(example1Binder);
    }

    public static IsWidget createExample2() {
        return new Examples(example2Binder);
    }

    public static IsWidget createExample3() {
        return new Examples(example3Binder);
    }

    public static IsWidget createExample4() {
        return new Examples(example4Binder);
    }

    public static IsWidget createExample5() {
        return new Examples(example5Binder);
    }

    public static IsWidget createExample6() {
        return new Examples(example6Binder);
    }

    public static IsWidget createExample7() {
        return new Examples(example7Binder);
    }

    public static IsWidget createExample8() {
        return new Examples(example8Binder);
    }

    public static IsWidget createExample9() {
        return new Examples(example9Binder);
    }

    public static IsWidget createExample10() {
        return new Examples(example10Binder);
    }

    public static IsWidget createExample11() {
        return new Examples(example11Binder);
    }

    public static IsWidget createExample12() {
        return new Examples(example12Binder);
    }

    public static IsWidget createExample13() {
        return new Examples(example13Binder);
    }

    public static IsWidget createExample14() {
        return new Examples(example14Binder);
    }

    public static IsWidget createExample15() {
        return new Examples(example15Binder);
    }

    public static IsWidget createExample16() {
        return new Examples(example16Binder);
    }

    public static IsWidget createExample17() {
        return new Examples(example17Binder);
    }

    public static IsWidget createExample18() {
        return new Examples(example18Binder);
    }

    public static IsWidget createExample19() {
        return new Examples(example19Binder);
    }

    public static IsWidget createExample20() {
        return new Examples(example20Binder);
    }

    public static IsWidget createExample21() {
        return new Examples(example21Binder);
    }

    public static IsWidget createExample22() {
        return new Examples(example22Binder);
    }

    public static IsWidget createExample23() {
        return new Examples(example23Binder);
    }

    public static IsWidget createExample24() {
        return new Examples(example24Binder);
    }

    public static IsWidget createExample25() {
        return new Examples(example25Binder);
    }

    public static IsWidget createExample26() {
        return new Examples(example26Binder);
    }

    public static IsWidget createExample27() {
        return new Examples(example27Binder);
    }

    public static IsWidget createExample28() {
        return new Examples(example28Binder);
    }

    public static IsWidget createExample29() {
        return new Examples(example29Binder);
    }
}
