import type { ProductDetail } from '../productDetailTypes'

type ProductStory = ProductDetail['productStories'][number]

type ProductStorySectionProps = {
  story: ProductStory
}

function ProductStorySection({ story }: ProductStorySectionProps) {
  const isImageRight = story.position % 2 === 0

  return (
    <article
      className={`mb-6 flex flex-col ${
        isImageRight ? 'md:flex-row-reverse' : 'md:flex-row'
      }`}
    >
      <div className="aspect-4/3 w-full shrink-0 overflow-hidden md:w-[38%]">
        <img
          src={story.imageUrl}
          alt=""
          className="h-full w-full object-cover"
        />
      </div>

      <div className="min-w-0 md:w-[45%]">
        <h3
          className={`mt-5 border-b border-border pb-2 text-left text-2xl font-bold text-accent ${
            isImageRight
              ? 'px-2 md:pl-4 md:pr-2 md:text-right'
              : 'px-2 md:pl-2 md:pr-4'
          }`}
        >
          {story.sectionTitle}
        </h3>

        <p
          className={`mt-3 whitespace-pre-wrap text-left leading-7 ${
            isImageRight
              ? 'px-2 md:pl-4 md:pr-2'
              : 'px-2 md:pl-2 md:pr-4'
          }`}
        >
          {story.body}
        </p>
      </div>
    </article>
  )
}

export default ProductStorySection