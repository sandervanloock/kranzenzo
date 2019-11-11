import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { KranzenzoTestModule } from '../../../test.module';
import { WorkshopSubscriptionDetailComponent } from 'app/entities/workshop-subscription/workshop-subscription-detail.component';
import { WorkshopSubscription } from 'app/shared/model/workshop-subscription.model';

describe('Component Tests', () => {
  describe('WorkshopSubscription Management Detail Component', () => {
    let comp: WorkshopSubscriptionDetailComponent;
    let fixture: ComponentFixture<WorkshopSubscriptionDetailComponent>;
    const route = ({ data: of({ workshopSubscription: new WorkshopSubscription(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KranzenzoTestModule],
        declarations: [WorkshopSubscriptionDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(WorkshopSubscriptionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(WorkshopSubscriptionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.workshopSubscription).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
