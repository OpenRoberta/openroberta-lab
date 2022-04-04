import Coordinates = JQuery.Coordinates;

interface JQuery {
    onWrap(element: string, callbackOrFilter: Function | string, callbackOrMessage?: Function | string): JQuery;

    oneWrap(element: string, callbackOrFilter: Function | string, callbackOrMessage?: Function | string): JQuery;

    clickWrap();

    openRightView($view: JQuery, initialViewWidth: number, opt_callBack?: () => void): void;

    closeRightView(opt_callBack?: () => void): void;

    draggable(opt: object): void;

    toggleSimPopup(position: Coordinates): void;

    bootstrapTable(option: any, ...args: any): any;
}
