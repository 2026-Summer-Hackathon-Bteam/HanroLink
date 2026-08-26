import type { components } from '../../shared/api/schema'

export type StoryFormData = {
  id?: number
  position: number
  productStorySectionTemplateId: number | ''
  imageFile: File | null
  existingImageUrl?: string
  body: string
}

export type ProductStoryTemplate =
  components['schemas']['ProductStorySectionTemplateOptionResponse']

export type StoryFormChanges = Partial<
  Omit<StoryFormData, 'id' | 'position' | 'existingImageUrl'>
>

export type SupplierProductCreateRequest =
  components['schemas']['SupplierProductCreateRequest']

export type ProductInformationFormData = Omit<
  SupplierProductCreateRequest,
  | 'mainImagePendingFileUploadId'
  | 'monthlySupplyCapacities'
  | 'productStories'
  | 'productCategoryId'
  | 'mainIngredientOriginPrefectureId'
  | 'expirationType'
  | 'shelfLifeDays'
  | 'storageType'
  | 'desiredRetailPrice'
  | 'unitsPerCase'
  | 'minimumOrderQuantity'
  | 'shippingLeadTimeDays'
> & {
  mainImageFile: File | null
  productCategoryId: number | ''
  mainIngredientOriginPrefectureId: number | ''
  expirationType: SupplierProductCreateRequest['expirationType'] | ''
  shelfLifeDays: number | ''
  storageType: SupplierProductCreateRequest['storageType'] | ''
  desiredRetailPrice: number | ''
  unitsPerCase: number | ''
  minimumOrderQuantity: number | ''
  shippingLeadTimeDays: number | ''
}

export type SupplierProductFormOptions =
  components['schemas']['SupplierProductFormOptionsResponse']

type MonthlySupplyCapacityRequest =
  components['schemas']['MonthlySupplyCapacityRequest']

export type MonthlySupplyCapacityFormData = Omit<
  MonthlySupplyCapacityRequest,
  'availableQuantity'
> & {
  availableQuantity: number | ''
}

export type ProductFormValues = {
  productInformations: ProductInformationFormData
  stories: StoryFormData[]
  monthlySupplyCapacities: MonthlySupplyCapacityFormData[]
}

export type ProductFormMode = 'create' | 'edit'

export type ProductFormInitialValues = ProductFormValues & {
  existingMainImageUrl?: string
}

export type ProductFormProps = {
  mode: ProductFormMode
  initialValues: ProductFormInitialValues
  onSubmit: (values: ProductFormValues) => void | Promise<void>
  onCancel?: () => void
  isSubmitting: boolean
  submitProgress: string
  submitError: string
}

export type ProductImageUploadRequest =
  components['schemas']['SupplierProductImageUploadCreateRequest']

export type ProductImageUploadResponse =
  components['schemas']['SupplierProductImageUploadCreateResponse']

export type ProductImageUsage = ProductImageUploadRequest['usage']

export type SupplierProductCreateResponse =
  components['schemas']['SupplierProductCreateResponse']

export type SupplierProductUpdateRequest =
  components['schemas']['SupplierProductUpdateRequest']
