/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { KranzenzoTestModule } from '../../../test.module';
import { WorkshopDateComponent } from 'app/entities/workshop-date/workshop-date.component';
import { WorkshopDateService } from 'app/entities/workshop-date/workshop-date.service';
import { WorkshopDate } from 'app/shared/model/workshop-date.model';

describe('Component Tests', () => {
    describe('WorkshopDate Management Component', () => {
        let comp: WorkshopDateComponent;
        let fixture: ComponentFixture<WorkshopDateComponent>;
        let service: WorkshopDateService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [KranzenzoTestModule],
                declarations: [WorkshopDateComponent],
                providers: []
            })
                .overrideTemplate(WorkshopDateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(WorkshopDateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WorkshopDateService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new WorkshopDate(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.workshopDates[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
