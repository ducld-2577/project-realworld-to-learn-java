package com.example.realworld.repository;

import com.example.realworld.model.Article;
import com.example.realworld.model.ArticleToTag;
import com.example.realworld.model.Tag;
// import com.example.realworld.model.Favorite;
import com.example.realworld.model.User;
import com.example.realworld.model.UserFavorite;
import com.example.realworld.model.UserFollow;
import com.example.realworld.repository.interfaces.ArticleRepositoryCustom;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Article> findArticlesWithFilters(String tag, String author, String favorited,
            int offset, int limit) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Article> cq = cb.createQuery(Article.class);
        Root<Article> article = cq.from(Article.class);

        List<Predicate> predicates = new ArrayList<>();

        // Tag filter
        if (tag != null && !tag.isEmpty()) {
            Join<Article, ArticleToTag> articleToTagJoin = article.join("articleToTags");
            Join<ArticleToTag, Tag> tagJoin = articleToTagJoin.join("tag");
            predicates.add(cb.equal(tagJoin.get("name"), tag));
        }

        // Author filter
        if (author != null && !author.isEmpty()) {
            Join<Article, User> authorJoin = article.join("author");
            predicates.add(cb.equal(authorJoin.get("username"), author));
        }

        // Favorited filter
        if (favorited != null && !favorited.isEmpty()) {
            Join<Article, UserFavorite> favoriteJoin = article.join("favorites");
            Join<UserFavorite, User> userJoin = favoriteJoin.join("user");
            predicates.add(cb.equal(userJoin.get("username"), favorited));
        }

        cq.select(article).where(cb.and(predicates.toArray(new Predicate[0])))
                .orderBy(cb.desc(article.get("createdAt")));

        TypedQuery<Article> query = entityManager.createQuery(cq);
        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.getResultList();
    }

    public List<Article> findArticlesOnFeedWithFilters(String tag, String author, String favorited,
            int offset, int limit, User currentUser) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Article> cq = cb.createQuery(Article.class);
        Root<Article> article = cq.from(Article.class);

        List<Predicate> predicates = new ArrayList<>();

        // Tag filter
        if (tag != null && !tag.isEmpty()) {
            Join<Article, ArticleToTag> articleToTagJoin = article.join("articleToTags");
            Join<ArticleToTag, Tag> tagJoin = articleToTagJoin.join("tag");
            predicates.add(cb.equal(tagJoin.get("name"), tag));
        }

        // Author filter
        if (author != null && !author.isEmpty()) {
            Join<Article, User> authorJoin = article.join("author");
            predicates.add(cb.equal(authorJoin.get("username"), author));
        }

        // Favorited filter
        if (favorited != null && !favorited.isEmpty()) {
            Join<Article, UserFavorite> favoriteJoin = article.join("favorites");
            Join<UserFavorite, User> userJoin = favoriteJoin.join("user");
            predicates.add(cb.equal(userJoin.get("username"), favorited));
        }

        if (currentUser != null) {
            Join<Article, User> articleAuthor = article.join("author");
            Subquery<User> followedSubquery = cq.subquery(User.class);
            Root<UserFollow> followRoot = followedSubquery.from(UserFollow.class);
            followedSubquery.select(followRoot.get("following"))
                    .where(cb.equal(followRoot.get("follower"), currentUser));
            predicates.add(articleAuthor.in(followedSubquery));
        }

        cq.select(article).where(cb.and(predicates.toArray(new Predicate[0])))
                .orderBy(cb.desc(article.get("createdAt")));

        TypedQuery<Article> query = entityManager.createQuery(cq);
        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.getResultList();
    }

}
