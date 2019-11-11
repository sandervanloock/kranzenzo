import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IWorkshopSubscription } from 'app/shared/model/workshop-subscription.model';
import { WorkshopSubscriptionService } from './workshop-subscription.service';

@Component({
  selector: 'jhi-workshop-subscription-delete-dialog',
  templateUrl: './workshop-subscription-delete-dialog.component.html'
})
export class WorkshopSubscriptionDeleteDialogComponent {
  workshopSubscription: IWorkshopSubscription;

  constructor(
    protected workshopSubscriptionService: WorkshopSubscriptionService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.workshopSubscriptionService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'workshopSubscriptionListModification',
        content: 'Deleted an workshopSubscription'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-workshop-subscription-delete-popup',
  template: ''
})
export class WorkshopSubscriptionDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ workshopSubscription }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(WorkshopSubscriptionDeleteDialogComponent as Component, {
          size: 'lg',
          backdrop: 'static'
        });
        this.ngbModalRef.componentInstance.workshopSubscription = workshopSubscription;
        this.ngbModalRef.result.then(
          () => {
            this.router.navigate(['/workshop-subscription', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          () => {
            this.router.navigate(['/workshop-subscription', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
