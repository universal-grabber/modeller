import {singleton} from "tsyringe";
import {PropertyOperationsService} from "./property-operations-service";
import {InspectionAlgorithm} from "./inspection-algorithm";
import {SingleInspectionAlgorithm} from "./single-inspection-algorithm";
import {SelectionHelper} from "./selection-helper";
import {ContextAwareService} from "./context-aware-service";
import {ToolTopService} from "./tool-top-service";
import {Example} from "../model/example";
import {EditorFieldData} from "../model/editor-field-data";
import {ContainerInspectionAlgorithm} from "./container-inspection-algorithm";
import {IterationInspectionAlgorithm} from "./iteration-inspection-algorithm";

@singleton()
export class Inspector {

    public iframeWindow: Window = window;
    private inspecting: boolean = false;
    private list: Element[] = [];
    private readonly $hoverListener: (e: any) => void;
    private readonly $clickListener: (e: any) => void;
    private lastHoveredElem: any;
    public example!: Example;

    private modificationCount: number = 0;

    public editPropertyDialog!: {
        setState: (params: any) => void;
    };

    public constructor(public propertyOperationsService: PropertyOperationsService,
                       public selectionHelper: SelectionHelper,
                       public contextAwareService: ContextAwareService,
                       public toolTopService: ToolTopService) {
        this.$hoverListener = this.hoverListener.bind(this);
        this.$clickListener = this.clickListener.bind(this);
    }

    public startInspection() {
        if (this.inspecting) {
            this.stopInspection();
        } else {
            this.iframeWindow.addEventListener("mouseover", this.$hoverListener);
            this.inspecting = true;
        }
    }

    public addTextInspection() {
        if (this.inspecting) {
            const selection = this.iframeWindow.getSelection();
            console.log(selection);
        }
    }

    changeExample(example: Example) {
        this.example = example;

        const fieldHandlers = Object.keys(this.example.properties);

        for (let fieldHandler of fieldHandlers) {
            this.toolTopService.preparePropertyToolTip(this, fieldHandler);
        }
    }

    clearInspectionClasses() {
        this.iframeWindow.document.querySelectorAll('.pHovered').forEach(elem => {
            elem.classList.remove('pHovered');
        });
        this.iframeWindow.document.querySelectorAll('.pSelected').forEach(elem => {
            elem.classList.remove('pSelected');
        })
    }

    hoverListener(event: any) {
        const target = event.target;
        if (target.classList.contains('inspection-ignore')) {
            return;
        }
        this.attach(target);

        if (this.lastHoveredElem) {
            this.unAttach(this.lastHoveredElem);
        }

        this.lastHoveredElem = target;
    }

    attach(target: any) {
        target.classList.add('pHovered');
        target.removeEventListener('click', this.$clickListener);
        target.addEventListener('click', this.$clickListener);
    }

    clickListener(event: any) {
        if (event.target.classList.contains('pSelected')) {
            this.list.splice(this.list.indexOf(event.target), 1);
            event.target.classList.remove('pSelected');
            return;
        }
        this.list.push(event.target);

        event.target.classList.add('pSelected');

        event.preventDefault();
        event.stopPropagation();
        event.stopImmediatePropagation();
    }

    unAttach(target: any) {
        target.classList.remove('pHovered');
        target.removeEventListener('click', this.$clickListener);
    }

    public stopInspection() {
        this.list = [];
        this.iframeWindow.removeEventListener('mouseover', this.$hoverListener);
        this.inspecting = false;
        if (this.lastHoveredElem) {
            this.unAttach(this.lastHoveredElem);
        }
    }

    public select(algorithmName: string) {
        try {
            const inspectionAlgorithm: InspectionAlgorithm = this.locateAlgorithm(algorithmName);

            this.clearInspectionClasses();

            const fieldHandler = inspectionAlgorithm.execute(this.list);

            this.stopInspection();

            this.toolTopService.preparePropertyToolTip(this, fieldHandler);
        } catch (e) {
            alert(e);
        }
    }

    private locateAlgorithm(algorithmName: string): InspectionAlgorithm {
        switch (algorithmName) {
            case 'single-inspector':
                return new SingleInspectionAlgorithm(this);
            case 'container-inspector':
                return new ContainerInspectionAlgorithm(this);
            case 'iteration-inspector':
                return new IterationInspectionAlgorithm(this);
            default:
                throw new Error();
        }
    }

    openPropertyEditor(fieldHandler: string) {
        const propertyData = this.example.properties[fieldHandler];

        const state = {
            isOpen: true,
            selector: propertyData.selectors.join(','),
            containerSelector: propertyData.containerSelectors.join(','),
            type: propertyData.type,
            field: fieldHandler,
            required: propertyData.required,
            propertyType: propertyData.propertyType,
            prevFieldName: fieldHandler
        };

        this.editPropertyDialog.setState(state);
    }

    public deleteProperty(fieldHandler: string) {
        this.toolTopService.removeTooltip(this, fieldHandler);
        this.propertyOperationsService.removeProperty(this, fieldHandler);
    }

    public saveProperty(fieldHandler: string, propertyEditorData: EditorFieldData) {
        // fieldHandler maybe old value, new value is propertyEditorData.field
        this.propertyOperationsService.updateSimpleProperty(this, propertyEditorData.field, {
            type: propertyEditorData.type,
            propertyType: propertyEditorData.propertyType,
            selectors: [propertyEditorData.selector],
            containerSelectors: [propertyEditorData.containerSelector],
            selectorType: 'CSS',
            required: propertyEditorData.required
        });

        this.modificationCount++;

        if (fieldHandler !== propertyEditorData.field) {
            this.toolTopService.renameToolTip(this, fieldHandler, propertyEditorData.field)
            delete this.example.properties[fieldHandler];
        }
    }
}
